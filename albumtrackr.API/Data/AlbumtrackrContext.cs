using albumtrackr.API.DTO;
using Microsoft.EntityFrameworkCore;

namespace albumtrackr.API.Data
{
    public class AlbumtrackrContext : DbContext
    {
        public AlbumtrackrContext(DbContextOptions<AlbumtrackrContext> options)
            : base(options)
        {
        }

        public DbSet<AlbumList> ALists { get; set; }

        public DbSet<Album> Albums { get; set; }

        //Add-Migration init
        //Update-Database
    }
}