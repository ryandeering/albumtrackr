using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using albumtrackr.API.Data;
using albumtrackr.API.DTO;
using IF.Lastfm.Core.Api;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;

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

        public async Task<AlbumList> GetById(int id)
        {
            return await _albumtrackrContext.ALists.Include("Albums").FirstOrDefaultAsync(al => al.Id == id);
        }

        public async Task<AlbumList> AddToList(int id, Album album)
        {
            var list = await _albumtrackrContext.ALists.FirstAsync(al => al.Id == id);

            if (list == null) return null;

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
    }
}