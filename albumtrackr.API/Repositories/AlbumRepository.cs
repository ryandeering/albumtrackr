using albumtrackr.API.Data;
using albumtrackr.API.DTO;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Extensions.Configuration;
using IF.Lastfm.Core.Api;
using System.Threading.Tasks;

namespace albumtrackr.API.Repositories
{
    public class AlbumRepository : IAlbumRepository
    {
        private readonly AlbumtrackrContext _albumtrackrContext;
        private readonly IConfiguration _configuration;

        public AlbumRepository(AlbumtrackrContext albumtrackrContext, IConfiguration configuration)
        {
            _albumtrackrContext = albumtrackrContext;
            _configuration = configuration;
        }

        public Album GetAlbum(int id)
        { 

            return _albumtrackrContext.Albums.FirstOrDefault(a => a.Id == id);
        }

        public async Task<List<Album>> AddAlbumAsync(Album foo)
        {
            string APIKey = _configuration.GetSection("LastFMApiKey").Value;
            string APISecret = _configuration.GetSection("LastFMApiSecret").Key;

            var client = new LastfmClient(APIKey, APISecret);

            var response = await client.Album.GetInfoAsync(foo.Artist, foo.Name);

            if(response.Content.Images.Medium != null)
            {
                foo.Thumbnail = response.Content.Images.Medium.ToString();
                foo.Name = response.Content.Name;
                foo.Artist = response.Content.ArtistName;
            }

            _albumtrackrContext.Albums.Add(foo);

            List<Album> albums = new List<Album>();
            albums.Add(foo);

            return albums;
         


        } //test method to see if the lastfm stuff works


    }
}
