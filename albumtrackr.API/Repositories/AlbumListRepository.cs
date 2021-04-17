using albumtrackr.API.Data;
using albumtrackr.API.DTO;
using IF.Lastfm.Core.Api;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace albumtrackr.API.Repositories
{
    public class AlbumListRepository : IAlbumListRepository
    {
        private readonly AlbumtrackrContext _albumtrackrContext;
        private readonly IConfiguration _configuration;

        public AlbumListRepository(AlbumtrackrContext albumtrackrContext, IConfiguration configuration)
        {
            _albumtrackrContext = albumtrackrContext;
            _configuration = configuration;
        }

        public async Task<List<AlbumList>> GetLatestLists()
        {
            return await _albumtrackrContext.ALists.OrderByDescending(al => al.Created).ToListAsync();
        }

        public async Task<List<AlbumList>> GetMyLists(string userName)
        {
            return await _albumtrackrContext.ALists.Where(al => al.Username == userName).ToListAsync();
        }

        public async Task<List<AlbumList>> GetPopularLists()
        {
            return await _albumtrackrContext.ALists.OrderBy(al => al.Stars).ToListAsync();
        }

        public async Task<AlbumList> GetById(int id)
        {
            return await _albumtrackrContext.ALists.Include("Albums").FirstOrDefaultAsync(al => al.Id == id);
        }

        public async Task<AlbumList> AddToList(int id, Album album)
        {
            var list = await _albumtrackrContext.ALists.Include("Albums").FirstAsync(al => al.Id == id);

            if (list == null) return null;

            if (_albumtrackrContext.Albums.Any(a => a.Artist.Equals(album.Artist) && a.Name.Equals(album.Name)))
            {
                var newAlbum = _albumtrackrContext.Albums.FirstOrDefault(a =>
                    a.Artist.Equals(album.Artist) && a.Name.Equals(album.Name));

                list.Albums.Add(newAlbum);
                await _albumtrackrContext.SaveChangesAsync();
                return list;
            }

            list.Albums ??= new List<Album>();

            var apiKey = _configuration.GetSection("LastFMApiKey").Value;
            var apiSecret = _configuration.GetSection("LastFMApiSecret").Key;

            var client = new LastfmClient(apiKey, apiSecret);

            var response = await client.Album.GetInfoAsync(album.Artist, album.Name);

            if ((response.Content.Name != null) | (response.Content.ArtistName != null))
            {
                album.Thumbnail = response.Content.Images.Largest.ToString();
                album.Name = response.Content.Name;
                album.Artist = response.Content.ArtistName;
            }
            else
            {
                return null;
            }

            list.Albums.Add(album);
            await _albumtrackrContext.SaveChangesAsync();

            return list;
        }


        // Delete from list, album seems to still be present
        public async Task<AlbumList> DeleteFromList(int id, int aid)
        {
            var list = await _albumtrackrContext.ALists.Include("Albums").FirstOrDefaultAsync(al => al.Id == id);

            var album = list.Albums.First(a => a.Id == aid);

            if (album == null) return null;

            list.Albums.Remove(album);
            await _albumtrackrContext.SaveChangesAsync();

            return list;
        }

        // Delete list
        public async Task<AlbumList> DeleteList(int id)
        {
            var list = await _albumtrackrContext.ALists.Include("Albums").FirstOrDefaultAsync(al => al.Id == id);

            if (list == null) return null;

            _albumtrackrContext.ALists.Remove(list);
            await _albumtrackrContext.SaveChangesAsync();

            return list;
        }

        public async Task<AlbumList> CreateAlbumList(string userName, string name, string description)
        {
            var list = new AlbumList
            {
                Username = userName,
                Name = name,
                Description = description,
                Albums = new List<Album>(),
                Created = DateTime.Now,
                Stars = 0
            };

            await _albumtrackrContext.ALists.AddAsync(list);
            await _albumtrackrContext.SaveChangesAsync();


            return list;
        }

        public async Task<AlbumList> EditDescription(int id, string name, string description)
        {
            var list = await _albumtrackrContext.ALists.Include("Albums").FirstOrDefaultAsync(al => al.Id == id);

            if (list == null) return null;

            if (description.Length <= 30)
            {
                list.Name = name;
                list.Description = description;
            }

            await _albumtrackrContext.SaveChangesAsync();
            return list;
        }


        // Find albumlist by ID, and increment the rating (stars)
        public async Task<AlbumList> StarAlbumList(int albumListId)
        {
            var list = await _albumtrackrContext.ALists.Include("Albums")
                .FirstOrDefaultAsync(al => al.Id == albumListId);

            if (list == null)
            {
                return null;
            }

            list.Stars += 1;
            _albumtrackrContext.ALists.Update(list);
            await _albumtrackrContext.SaveChangesAsync();
            return list;
        }
    }
}