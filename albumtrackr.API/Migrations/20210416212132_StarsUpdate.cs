using Microsoft.EntityFrameworkCore.Migrations;

namespace albumtrackr.API.Migrations
{
    public partial class StarsUpdate : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Stars_ALists_albumListId",
                table: "Stars");

            migrationBuilder.DropIndex(
                name: "IX_Stars_albumListId",
                table: "Stars");

            migrationBuilder.RenameColumn(
                name: "albumListId",
                table: "Stars",
                newName: "AlbumListId");

            migrationBuilder.AddColumn<int>(
                name: "Stars",
                table: "ALists",
                type: "int",
                nullable: false,
                defaultValue: 0);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Stars",
                table: "ALists");

            migrationBuilder.RenameColumn(
                name: "AlbumListId",
                table: "Stars",
                newName: "albumListId");

            migrationBuilder.CreateIndex(
                name: "IX_Stars_albumListId",
                table: "Stars",
                column: "albumListId");

            migrationBuilder.AddForeignKey(
                name: "FK_Stars_ALists_albumListId",
                table: "Stars",
                column: "albumListId",
                principalTable: "ALists",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
