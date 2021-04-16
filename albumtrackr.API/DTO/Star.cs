using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace albumtrackr.API.DTO
{
    public class Star
    {
        public int Id { get; set; }

        public string Username { get; set; }

        public int AlbumListId { get; set; }

    }
}
