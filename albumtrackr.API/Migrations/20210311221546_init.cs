using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace albumtrackr.API.Migrations
{
    public partial class init : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                "Albums",
                table => new
                {
                    Id = table.Column<int>("int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Artist = table.Column<string>("nvarchar(max)", nullable: true),
                    Name = table.Column<string>("nvarchar(max)", nullable: true),
                    Thumbnail = table.Column<string>("nvarchar(max)", nullable: true)
                },
                constraints: table => { table.PrimaryKey("PK_Albums", x => x.Id); });

            migrationBuilder.CreateTable(
                "ALists",
                table => new
                {
                    Id = table.Column<int>("int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    created = table.Column<DateTime>("datetime2", nullable: false),
                    Username = table.Column<string>("nvarchar(max)", nullable: true),
                    Name = table.Column<string>("nvarchar(max)", nullable: true),
                    Description = table.Column<string>("nvarchar(max)", nullable: true),
                    Stars = table.Column<int>("int", nullable: false)
                },
                constraints: table => { table.PrimaryKey("PK_ALists", x => x.Id); });

            migrationBuilder.CreateTable(
                "AlbumAlbumList",
                table => new
                {
                    AlbumsId = table.Column<int>("int", nullable: false),
                    ListsId = table.Column<int>("int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AlbumAlbumList", x => new {x.AlbumsId, x.ListsId});
                    table.ForeignKey(
                        "FK_AlbumAlbumList_Albums_AlbumsId",
                        x => x.AlbumsId,
                        "Albums",
                        "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        "FK_AlbumAlbumList_ALists_ListsId",
                        x => x.ListsId,
                        "ALists",
                        "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                "IX_AlbumAlbumList_ListsId",
                "AlbumAlbumList",
                "ListsId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                "AlbumAlbumList");

            migrationBuilder.DropTable(
                "Albums");

            migrationBuilder.DropTable(
                "ALists");
        }
    }
}