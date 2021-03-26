using Microsoft.EntityFrameworkCore.Migrations;

namespace albumtrackr.API.Migrations
{
    public partial class AlbumListChange : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "AlbumAlbumList");

            migrationBuilder.AddColumn<int>(
                name: "AlbumListId",
                table: "Albums",
                type: "int",
                nullable: true);

            migrationBuilder.CreateIndex(
                name: "IX_Albums_AlbumListId",
                table: "Albums",
                column: "AlbumListId");

            migrationBuilder.AddForeignKey(
                name: "FK_Albums_ALists_AlbumListId",
                table: "Albums",
                column: "AlbumListId",
                principalTable: "ALists",
                principalColumn: "Id",
                onDelete: ReferentialAction.Restrict);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Albums_ALists_AlbumListId",
                table: "Albums");

            migrationBuilder.DropIndex(
                name: "IX_Albums_AlbumListId",
                table: "Albums");

            migrationBuilder.DropColumn(
                name: "AlbumListId",
                table: "Albums");

            migrationBuilder.CreateTable(
                name: "AlbumAlbumList",
                columns: table => new
                {
                    AlbumsId = table.Column<int>(type: "int", nullable: false),
                    ListsId = table.Column<int>(type: "int", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_AlbumAlbumList", x => new { x.AlbumsId, x.ListsId });
                    table.ForeignKey(
                        name: "FK_AlbumAlbumList_Albums_AlbumsId",
                        column: x => x.AlbumsId,
                        principalTable: "Albums",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_AlbumAlbumList_ALists_ListsId",
                        column: x => x.ListsId,
                        principalTable: "ALists",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_AlbumAlbumList_ListsId",
                table: "AlbumAlbumList",
                column: "ListsId");
        }
    }
}
