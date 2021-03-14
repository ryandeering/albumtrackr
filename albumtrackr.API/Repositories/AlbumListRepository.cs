using albumtrackr.API.Data;
using albumtrackr.API.DTO;
using System.Collections.Generic;
using System.Linq;

namespace albumtrackr.API.Repositories
{
    public class AlbumListRepository : IAlbumListRepository
    {
        private readonly AlbumtrackrContext _albumtrackrContext;

        public AlbumListRepository(AlbumtrackrContext albumtrackrContext)
        {
            _albumtrackrContext = albumtrackrContext;
        }

        public IEnumerable<AlbumList> GetLatestLists()
        {
            return _albumtrackrContext.ALists.OrderByDescending(al => al.created).Take(5);
        }




    }
}
