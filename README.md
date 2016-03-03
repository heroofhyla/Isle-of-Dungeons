Isle Of Dungeons Editor, or "IsleEdit" is a 2d map editor, intended for games with tile-based graphics. It supports tiles of any size, as long as they're square. To define the size of a map, first define the tile size. Then define how many tiles can fit on screen at once. Finally, define the number of screens.

This is a work in progress that receives only sporadic updates. It still lacks complex drawing tools, like a rectangle tool or a fill bucket. All it has right now is a pen tool (left click), and an eye dropper (right click). Holding shift lets you override default autotiling behavior.

Saving and loading works. It exports to a custom binary file format. See the comments on SaveAction in MainWindow.java for details.

Known issues:
Behavior when you right click and drag is undefined.
Map is destroyed if you make any changes to the settings. I need to decide how I want to handle things like changing the dimensions of a map nondestructively.
The grid appears to be one pixel out of alignment.
If you add columns to a tileset, it will change the tile IDs for all tiles past the first line. Thus, existing maps will show the wrong tiles. I need to come up with some sort of conversion tool.
