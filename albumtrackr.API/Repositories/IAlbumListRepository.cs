﻿using albumtrackr.API.DTO;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace albumtrackr.API.Repositories
{
    public interface IAlbumListRepository
    {
        IEnumerable<AlbumList> GetLatestLists();



    }
}
