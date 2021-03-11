using Microsoft.EntityFrameworkCore;
using System.Collections.Generic;

namespace albumtrackr.API.DTO
{ //experimental
    public class Album
    {
        public int Id { get; set; }

        public string Artist { get; set; }

        public string Name { get; set; }

        public string Thumbnail { get; set; }

        public virtual ICollection<AlbumList> Lists { get; set; }

    }
}
