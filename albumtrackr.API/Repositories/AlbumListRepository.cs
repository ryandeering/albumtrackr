using albumtrackr.API.Data;
using albumtrackr.API.DTO;
using System.Collections.Generic;

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
            return _albumtrackrContext.ALists;
        }
    }
}
