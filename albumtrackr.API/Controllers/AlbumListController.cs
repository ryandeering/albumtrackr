using albumtrackr.API.DTO;
using albumtrackr.API.Repositories;
using Microsoft.AspNetCore.Mvc;

namespace _4thYearProject.Api.Controllers
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
        public IActionResult GetLatestLists()
        {
            return Ok(_albumListRepository.GetLatestLists());
        }

        //[HttpGet("{id}")]
        //public IActionResult GetEmployeeById(int id)
        //{
        //    return Ok(_employeeRepository.GetEmployeeById(id));
        //}

        //[HttpPost]
        //public IActionResult CreateEmployee([FromBody] Employee employee)
        //{
        //    if (employee == null)
        //        return BadRequest();

        //    if (employee.FirstName == string.Empty || employee.LastName == string.Empty)
        //    {
        //        ModelState.AddModelError("Name/FirstName", "The name or first name shouldn't be empty");
        //    }

        //    if (!ModelState.IsValid)
        //        return BadRequest(ModelState);

        //    var createdEmployee = _employeeRepository.AddEmployee(employee);

        //    return Created("employee", createdEmployee);
        //}

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
