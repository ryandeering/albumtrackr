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

        [HttpGet("userName")]
        public async Task<IActionResult> GetMyLists(string userName)
        {
            if (userName == null) return BadRequest();

            var userLists = await _albumListRepository.GetMyLists(userName);

            return Ok(userLists);
        }

        [HttpGet("id")]
        public async Task<IActionResult> GetById(int id)
        {
            var userList = await _albumListRepository.GetById(id);

            if (userList == null) return NotFound();

            return Ok(userList);
        }

        [HttpPost]
        public async Task<IActionResult> AddToList(int id, [FromBody] Album album)
        {
            if (album == null) return BadRequest();

            if (album.Artist == string.Empty || album.Name == string.Empty)
                ModelState.AddModelError("Artist/Album Name", "The artist or album name is not correct.");

            if (!ModelState.IsValid) return BadRequest(ModelState);

            var userList = await _albumListRepository.AddToList(id, album);

            return Ok(userList);
        }

        [HttpPost("albumList")]
        public async Task<IActionResult> CreateAlbumList([FromBody] AlbumList albumList)
        {
            var list = await _albumListRepository.CreateAlbumList(albumList.Username, albumList.Name,
                albumList.Description);

            if (list == null) return BadRequest();

            return Ok(list);
        }
    }
}