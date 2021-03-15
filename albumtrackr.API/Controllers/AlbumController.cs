using System.Text.Json;
using albumtrackr.API.DTO;
using albumtrackr.API.Repositories;
using Microsoft.AspNetCore.Mvc;

namespace _4thYearProject.Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AlbumController : Controller
    {
        private readonly IAlbumRepository _albumRepository;

        public AlbumController(IAlbumRepository albumRepository)
        {
            _albumRepository = albumRepository;
        }

        [HttpGet("{id}")]
        public IActionResult GetAlbum(int id)
        {
            return Ok(_albumRepository.GetAlbum(id));
        }

        //[HttpGet("{id}")]
        //public IActionResult GetEmployeeById(int id)
        //{
        //    return Ok(_employeeRepository.GetEmployeeById(id));
        //}

        [HttpPost]
        public ActionResult<Album> CreateAlbum(string Name, string Artist)
        {
            if (Name == null)
                return BadRequest();

            if (Name == string.Empty || Artist == string.Empty)
            {
                ModelState.AddModelError("AlbumName/ArtistName", "The album name or artist name shouldn't be empty");
            }

            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            Album foo = new Album
            {
                Artist = Artist,
                Name = Name
            };

            var createdAlbum = _albumRepository.AddAlbumAsync(foo);

            return Ok(createdAlbum.Result);
        }

        //[HttpPut]
        //public IActionResult UpdateEmployee([FromBody] Employee employee)
        //{
        //    if (employee == null)
        //        return BadRequest();

        //    if (employee.FirstName == string.Empty || employee.LastName == string.Empty)
        //    {
        //        ModelState.AddModelError("Name/FirstName", "The name or first name shouldn't be empty");
        //    }

        //    if (!ModelState.IsValid)
        //        return BadRequest(ModelState);

        //    var employeeToUpdate = _employeeRepository.GetEmployeeById(employee.EmployeeId);

        //    if (employeeToUpdate == null)
        //        return NotFound();

        //    _employeeRepository.UpdateEmployee(employee);

        //    return NoContent(); //success
        //}

        //[HttpDelete("{id}")]
        //public IActionResult DeleteEmployee(int id)
        //{
        //    if (id == 0)
        //        return BadRequest();

        //    var employeeToDelete = _employeeRepository.GetEmployeeById(id);
        //    if (employeeToDelete == null)
        //        return NotFound();

        //    _employeeRepository.DeleteEmployee(id);

        //    return NoContent();//success
        //}
    }
}
