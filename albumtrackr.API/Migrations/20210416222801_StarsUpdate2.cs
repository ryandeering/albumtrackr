using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace albumtrackr.API.Migrations
{
    public partial class StarsUpdate2 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                "ALists",
                table => new
                {
                    Id = table.Column<int>("int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Created = table.Column<DateTime>("datetime2", nullable: false),
                    Username = table.Column<string>("nvarchar(max)", nullable: true),
                    Name = table.Column<string>("nvarchar(max)", nullable: true),
                    Description = table.Column<string>("nvarchar(max)", nullable: true),
                    Stars = table.Column<int>("int", nullable: false)
                },
                constraints: table => { table.PrimaryKey("PK_ALists", x => x.Id); });

            migrationBuilder.CreateTable(
                "Stars",
                table => new
                {
                    Id = table.Column<int>("int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Username = table.Column<string>("nvarchar(max)", nullable: true),
                    AlbumListId = table.Column<int>("int", nullable: false)
                },
                constraints: table => { table.PrimaryKey("PK_Stars", x => x.Id); });

            migrationBuilder.CreateTable(
                "Albums",
                table => new
                {
                    Id = table.Column<int>("int", nullable: false)
                        .Annotation("SqlServer:Identity", "1, 1"),
                    Artist = table.Column<string>("nvarchar(max)", nullable: true),
                    Name = table.Column<string>("nvarchar(max)", nullable: true),
                    Thumbnail = table.Column<string>("nvarchar(max)", nullable: true),
                    AlbumListId = table.Column<int>("int", nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Albums", x => x.Id);
                    table.ForeignKey(
                        "FK_Albums_ALists_AlbumListId",
                        x => x.AlbumListId,
                        "ALists",
                        "Id",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateIndex(
                "IX_Albums_AlbumListId",
                "Albums",
                "AlbumListId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                "Albums");

            migrationBuilder.DropTable(
                "Stars");

            migrationBuilder.DropTable(
                "ALists");
        }
    }
}