using System.Threading.Tasks;
using albumtrackr.API.DTO;
using albumtrackr.API.Repositories;
using Microsoft.AspNetCore.Mvc;

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
            if (albumList == null) return BadRequest();

            var list = await _albumListRepository.CreateAlbumList(albumList.Username, albumList.Name,
                albumList.Description);

            if (list == null) return BadRequest();

            return Ok(list);
        }

        // star an album by id
        [HttpPut("{albumListId}")]
        public async Task<IActionResult> StarAlbumList(int albumListId)
        {
            var userList = await _albumListRepository.StarAlbumList(albumListId);

            if (userList == null) return NotFound();

            return Ok(userList);
        }

        [HttpDelete("{id}/album/{aid}")]
        public async Task<IActionResult> DeleteFromList(int id, int aid)
        {
            var userList = await _albumListRepository.DeleteFromList(id, aid);

            if (userList == null) return NotFound();

            return Ok(userList);
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteList(int id)
        {
            var userList = await _albumListRepository.DeleteList(id);

            if (userList == null) return NotFound();


            return Ok(userList);
        }

        [HttpPut("{id}/{name}/{description}")]
        public async Task<IActionResult> EditDescription(int id, string name, string description)
        {
            var userList = await _albumListRepository.EditDescription(id, name, description);

            if (userList == null) return NotFound();

            return Ok(userList);
        }
    }
}