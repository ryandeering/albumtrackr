using Microsoft.EntityFrameworkCore;

namespace albumtrackr.API.Data
{
    public class AlbumtrackrContext : DbContext
    {
        public AlbumtrackrContext(DbContextOptions<AlbumtrackrContext> options)
            : base(options)
        {
        }

        //public DbSet<StockListing> StockListing { get; set; } //change

        //Add-Migration init
        //Update-Database
    }
}
