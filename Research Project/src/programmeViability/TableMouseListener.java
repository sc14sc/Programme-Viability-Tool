package programmeViability;
//http://www.codejava.net/java-se/swing/jtable-popup-menu-example

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

public class TableMouseListener extends MouseAdapter {
	
	private JTable table;

	public TableMouseListener(JTable table) {
		// TODO Auto-generated constructor stub
		this.table = table;
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		Point point = event.getPoint();
		int currentRow = table.rowAtPoint(point);
		table.setRowSelectionInterval(currentRow, currentRow);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		Point point = event.getPoint();
		int currentRow = table.rowAtPoint(point);
		table.setRowSelectionInterval(currentRow, currentRow);
	}

}
