package com.rugobal.dt.gui.thirdparty.groupabletableheader;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * GroupableTableHeader
 *
 * @version 1.0 10/20/98
 * @author Nobuo Tamemasa
 */

public class GroupableTableHeader extends JTableHeader {
	private static final String uiClassID = "GroupableTableHeaderUI";
	protected Vector columnGroups = null;

	public GroupableTableHeader(TableColumnModel model) {
		super(model);
		setUI(new GroupableTableHeaderUI());
		setReorderingAllowed(false);
	}
	public void updateUI(){
		setUI(new GroupableTableHeaderUI());
	}

	public void setReorderingAllowed(boolean b) {
		reorderingAllowed = false;
	}

	public void addColumnGroup(ColumnGroup g) {
		if (columnGroups == null) {
			columnGroups = new Vector();
		}
		columnGroups.addElement(g);
	}

	public Enumeration getColumnGroups(TableColumn col) {
		if (columnGroups == null) return null;
		Enumeration e = columnGroups.elements();
		while (e.hasMoreElements()) {
			ColumnGroup cGroup = (ColumnGroup)e.nextElement();
			Vector v_ret = (Vector)cGroup.getColumnGroups(col,new Vector());
			if (v_ret != null) { 
				return v_ret.elements();
			}
		}
		return null;
	}

	public void setColumnMargin() {
		if (columnGroups == null) return;
		int columnMargin = getColumnModel().getColumnMargin();
		Enumeration e = columnGroups.elements();
		while (e.hasMoreElements()) {
			ColumnGroup cGroup = (ColumnGroup)e.nextElement();
			cGroup.setColumnMargin(columnMargin);
		}
	}
	
	
	/**
	 * Ruben Gomez -> habria que ajustar dependiendo del numero de objectos Column Group anidados (cuantas "filas" tiene el header) 
	 */
	@Override
	public Dimension getPreferredSize() {
	    Dimension d = super.getPreferredSize();
	    d.height = 44; // (2 filas por 22 pixeles cada una de alto )
	    return d;
	}

}