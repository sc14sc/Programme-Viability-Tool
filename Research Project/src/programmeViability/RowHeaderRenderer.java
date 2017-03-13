/**
 * 
 */
package programmeViability;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author Sitong Chen
 *
 */
public class RowHeaderRenderer implements TableCellRenderer {
//http://stackoverflow.com/questions/6644739/customizing-jtable-cellrenderer-with-tables-cell-header-color
	
	TableCellRenderer renderer;
	
	public RowHeaderRenderer(JTable table) {
		renderer = table.getTableHeader().getDefaultRenderer();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// TODO Auto-generated method stub
		return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
}
