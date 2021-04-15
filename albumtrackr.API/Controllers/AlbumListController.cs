using System.Threading.Tasks;
using albumtrackr.API.DTO;
using albumtrackr.API.Repositories;
using Microsoft.AspNetCore.Mvc;
using System.Linq;
using Microsoft.AspNetCore.Http;
using System.Linq;

namespace albumtrackr.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AlbumListController : Controller
    {
        private readonly IAlbumListRepository _albumListRepository;

        public AlbumListController(IAlbumListRepository albumListRepository)
        {
            _albumListRepository = albumListRepository;
        }

        [HttpGet]
        public async Task<IActionResult> GetLatestLists()
        {
            return Ok(await _albumListRepository.GetLatestLists());
        }


        [HttpGet("popular/")]
        public async Task<IActionResult> GetPopularLists()
        {
            return Ok(await _albumListRepository.GetPopularLists());
        }

        [HttpGet("username/{userName}")]
        public async Task<IActionResult> GetMyLists(string userName)
        {
            if (userName == null) return BadRequest();

            var userLists = await _albumListRepository.GetMyLists(userName);

            if (userLists == null) return NoContent();

            return Ok(userLists);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(int id)
        {
            var userList = await _albumListRepository.GetById(id);

            if (userList == null) return NotFound();

            return Ok(userList);
        }


        [HttpPost("{id}/album/")]
        public async Task<IActionResult> AddToList(int id, [FromBody] Album album)
        {
            if (album == null) return BadRequest();

            if (album.Artist == string.Empty || album.Name == string.Empty)
                ModelState.AddModelError("Artist/Album Name", "The artist or album name is not correct.");

            if (!ModelState.IsValid) return BadRequest(ModelState);

            var userList = await _albumListRepository.AddToList(id, album);

            return Ok(userList);
        }

        [HttpPost]
        public async Task<IActionResult> CreateAlbumList([FromBody] AlbumList albumList)
        {
            var list = await _albumListRepository.CreateAlbumList(albumList.Username, albumList.Name,
                albumList.Description);

            if (list == null) return BadRequest();

            return Ok(list);
        }


        // star an album by id
        [HttpPost("{albumListId}/{username}")]
        [ProducesResponseType(StatusCodes.Status404NotFound)]           // not found
        [ProducesResponseType(StatusCodes.Status204NoContent)]          // ok no content
        public async Task<IActionResult> StarAlbumList(string username, int albumListId)
        {

            if (albumListId == null) return BadRequest();

            var userList = await _albumListRepository.StarAlbumList(username, albumListId);

            return Ok(userList);
        }

        [HttpDelete("{id}/album/{aid}")]
        public async Task<IActionResult> DeleteFromList(int id, int aid)
        {
            if (aid == null) return BadRequest();

            var userList = await _albumListRepository.DeleteFromList(id, aid);

            return Ok(userList);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteList(int id)
        {
            if (id == null) return BadRequest();

            var userList = await _albumListRepository.DeleteList(id);

            return Ok(userList);
        }

        [HttpPut("{id}")]
        public async Task<IActionResult> EditDescription(int id)
        {

            var userList = await _albumListRepository.EditDescription(id);

            return Ok(userList);
        }

       [HttpGet("{albumListId}/{username}")]
        public async Task<bool> ListAlreadyStarred(int albumListId, string username)
        {

        if (username == null) return false;

         var userList = await _albumListRepository.ListAlreadyStarred(albumListId, username);

            return userList;
        }

    }
}