using Microsoft.EntityFrameworkCore.Migrations;

namespace albumtrackr.API.Migrations
{
    public partial class AlbumListChange : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                "AlbumAlbumList");

            migrationBuilder.AddColumn<int>(
                "AlbumListId",
                "Albums",
                "int",
                nullable: true);

            migrationBuilder.CreateIndex(
                "IX_Albums_AlbumListId",
                "Albums",
                "AlbumListId");

            migrationBuilder.AddForeignKey(
                "FK_Albums_ALists_AlbumListId",
                "Albums",
                "AlbumListId",
                "ALists",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                "FK_Albums_ALists_AlbumListId",
                "Albums");

            migrationBuilder.DropIndex(
                "IX_Albums_AlbumListId",
                "Albums");

            migrationBuilder.DropColumn(
                "AlbumListId",
                "Albums");

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
    }
}