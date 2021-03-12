using albumtrackr.API.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace albumtrackr.API.Repositories
{
    public interface IAlbumRepository
    {
        Album GetAlbum(int id);
        Task<Album> AddAlbumAsync(Album foo);
    }
}
