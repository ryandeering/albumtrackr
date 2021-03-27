using System.Collections.Generic;
using System.Threading.Tasks;
using albumtrackr.API.DTO;

namespace albumtrackr.API.Repositories
{
    public interface IAlbumListRepository
    {
        Task<List<AlbumList>> GetLatestLists();
        Task<List<AlbumList>> GetMyLists(string userName);
        Task<AlbumList> AddToList(int id, Album album);
        Task<AlbumList> DeleteFromList(int id, int aid);
        Task<AlbumList> GetById(int id);
        Task<AlbumList> CreateAlbumList(string userName, string name, string description);
        // Task<AlbumList> PutStarAlbum(int id, string stars); // Need to discuss if this should be it's own data model
    }
}