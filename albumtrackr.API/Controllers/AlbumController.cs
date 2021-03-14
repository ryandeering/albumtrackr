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
        public IActionResult CreateAlbum([FromBody] Album foo)
        {
            if (foo.Name == null)
                return BadRequest();

            if (foo.Name == string.Empty || foo.Artist == string.Empty)
            {
                ModelState.AddModelError("AlbumName/ArtistName", "The album name or artist name shouldn't be empty");
            }

            if (!ModelState.IsValid)
                return BadRequest(ModelState);


            var createdAlbum = _albumRepository.AddAlbumAsync(foo);

            return Created("album", createdAlbum);
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
