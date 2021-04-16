namespace albumtrackr.API.Data
{
    using DTO;
    using Microsoft.EntityFrameworkCore;

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
