using System;
using albumtrackr.API.Data;
using albumtrackr.API.DTO;
using albumtrackr.API.Repositories;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Xunit;

namespace albumtrackr.UnitTests
{
    public class AlbumRepositoryUnitTests
    {



        [Fact]
        public void GetLatestLists_Test()
        {
            var albumListTestData = GenFu.GenFu.ListOf<AlbumList>(3);
            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            using var context = new AlbumtrackrContext(options);
            foreach (var albumList in albumListTestData) context.ALists.Add(albumList);
            context.SaveChanges();
            var repo = new AlbumListRepository(context, configuration);

            Assert.Equal(repo.GetLatestLists().Result, albumListTestData.OrderByDescending(a => a.Created));
            //Asynchronous blocking code IS bad, but in the case of a unit test, no harm done.
            context.ChangeTracker.Clear();
        }

        [Fact]
        public void GetMyLists_Test()
        {
            GenFu.GenFu.Configure<AlbumList>().Fill(al => al.Username, "TESTID");
            var albumListTestData = GenFu.GenFu.ListOf<AlbumList>(3);
            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            using var context = new AlbumtrackrContext(options);
            foreach (var albumList in albumListTestData) context.ALists.Add(albumList);
            context.SaveChanges();
            var repo = new AlbumListRepository(context, configuration);

            Assert.Equal(repo.GetMyLists("TESTID").Result, albumListTestData);
            context.ChangeTracker.Clear();
        }

        [Fact]
        public void GetPopularLists_Test()
        {
            var albumListTestData = GenFu.GenFu.ListOf<AlbumList>(3);
            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            using var context = new AlbumtrackrContext(options);
            foreach (var albumList in albumListTestData) context.ALists.Add(albumList);
            context.SaveChanges();
            var repo = new AlbumListRepository(context, configuration);

            Assert.Equal(repo.GetPopularLists().Result, albumListTestData.OrderByDescending(al => al.Stars));
            context.ChangeTracker.Clear();
        }

        [Fact]
        public void GetById_Test()
        {
            var albumListTestData = GenFu.GenFu.ListOf<AlbumList>(3);
            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            using var context = new AlbumtrackrContext(options);
            foreach (var albumList in albumListTestData) context.ALists.Add(albumList);
            context.SaveChanges();
            var repo = new AlbumListRepository(context, configuration);

            foreach (var albumList in albumListTestData)
            {
                Assert.Equal(repo.GetById(albumList.Id).Result, albumList);
            }

            context.ChangeTracker.Clear();
        }


        [Fact]
        public async Task DeleteFromList_Test()
        {
            var i = 1000;
            GenFu.GenFu.Configure<Album>()
                .Fill(a => a.Id, () => i++);

            GenFu.GenFu.Configure<AlbumList>().Fill(al => al.Albums);
            var album = GenFu.GenFu.New<AlbumList>();
            album.Albums = GenFu.GenFu.ListOf<Album>(5);

            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            await using var context = new AlbumtrackrContext(options);
            await context.ALists.AddAsync(album);
            await context.SaveChangesAsync();
            var repo = new AlbumListRepository(context, configuration);


            await repo.DeleteFromList(album.Id, album.Albums.First().Id);

            Assert.Equal(4, album.Albums.Count);
            context.ChangeTracker.Clear();
        }

        [Fact]
        public async Task DeleteList_Test()
        {

            GenFu.GenFu.Configure<AlbumList>().Fill(al => al.Albums);
            var album = GenFu.GenFu.New<AlbumList>();
            album.Albums = GenFu.GenFu.ListOf<Album>(5);

            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            await using var context = new AlbumtrackrContext(options);
            await context.ALists.AddAsync(album);
            await context.SaveChangesAsync();
            var repo = new AlbumListRepository(context, configuration);


            await repo.DeleteList(album.Id);

            Assert.Equal(0, context.ALists.Count());
            context.ChangeTracker.Clear();
        }

        [Fact]
        public async Task CreateAlbumList_Test()
        {
            var album = GenFu.GenFu.New<AlbumList>();

            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            await using var context = new AlbumtrackrContext(options);
            await context.ALists.AddAsync(album);
            await context.SaveChangesAsync();
            var repo = new AlbumListRepository(context, configuration);


           var created = await repo.CreateAlbumList(album.Username, album.Name, album.Description);

            Assert.Equal(album.Username, created.Username);
            context.ChangeTracker.Clear();

        }

        [Fact]
        public async Task EditDescription_Test()
        {
            var album = GenFu.GenFu.New<AlbumList>();

            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            await using var context = new AlbumtrackrContext(options);
            await context.ALists.AddAsync(album);
            await context.SaveChangesAsync();
            var repo = new AlbumListRepository(context, configuration);

           var successItem = await repo.EditDescription(album.Id, "hehe", "test");

            Assert.Equal(album.Name, successItem.Name);
            context.ChangeTracker.Clear();

        }

        [Fact]
        public async Task EditDescription_FAIL_Test()
        {
            var album = GenFu.GenFu.New<AlbumList>();

            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            await using var context = new AlbumtrackrContext(options);
            await context.ALists.AddAsync(album);
            await context.SaveChangesAsync();
            var repo = new AlbumListRepository(context, configuration);

            var failItem = await repo.EditDescription(album.Id, "jasdhsajjkdhaskjhdasjkhdkajshdjdasjkhdkajshdjdasjkhdkajshdjkashdjkhsaasdaskljdklasdkljaslkdjaslkjdaskljdklasjdklasjdkljaskldjaskljdas", "djkshasjkdhsakjhdaskjhdjkashdjkashjkdhasdasjkhdkajshdjdasjkhdkajshdjdasjkhdkajshdjdasjkhdkajshdjdasjkhdkajshdjjkdhasjkhdasjkhdakjshdjkashdjkash");

            Assert.Equal(album, failItem);
            context.ChangeTracker.Clear();
        }

        [Fact]
        public async Task StarAlbumList_Test()
        {
            var album = GenFu.GenFu.New<AlbumList>();

            var options = new DbContextOptionsBuilder<AlbumtrackrContext>()
                .UseInMemoryDatabase(Guid.NewGuid().ToString())
                .Options;


            Dictionary<string, string> inMemorySettings = new Dictionary<string, string>
            {
                {"TopLevelKey", "TopLevelValue"},
                {"SectionName:SomeKey", "SectionValue"},
            };

            IConfiguration configuration = new ConfigurationBuilder()
                .AddInMemoryCollection(inMemorySettings)
                .Build();

            await using var context = new AlbumtrackrContext(options);
            await context.ALists.AddAsync(album);
            await context.SaveChangesAsync();
            var repo = new AlbumListRepository(context, configuration);


            Assert.NotEqual(album.Stars, repo.StarAlbumList(album.Id).Result.Stars);
            context.ChangeTracker.Clear();

        }


    }
}