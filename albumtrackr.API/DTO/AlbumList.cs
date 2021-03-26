namespace albumtrackr.API.DTO
{
    using System;
    using System.Collections.Generic;

    public class AlbumList
    {
        public int Id { get; set; }

        public virtual ICollection<Album> Albums { get; set; }

        public DateTime Created { get; set; }

        public string Username { get; set; }

        public string Name { get; set; }

        public string Description { get; set; }

        public int Stars { get; set; } //should this be it's own datamodel?
    }
}
