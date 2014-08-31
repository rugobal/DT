package com.rugobal.dt.gui.thirdparty.tableheaderspan;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TableHeaderExample	{
	public static void main(String[] args)
	{
		DefaultTableModel data = new DefaultTableModel(10, 0);

		data.addColumn("ABC");
		data.addColumn("DEF");
		data.addColumn("GHI");
		data.addColumn("JKL");
		data.addColumn("MNO");
		data.addColumn("PQR");

		TableColumnModel columns = new DefaultTableColumnModel();

		TableHeader.XTableColumn abc = new TableHeader.XTableColumn();
		abc.setHeaderValue("ABC");
		abc.setHeaderSpan(2);

		TableColumn ghi = new TableColumn(2);
		ghi.setHeaderValue("GHI");

		TableHeader.XTableColumn jkl = new TableHeader.XTableColumn();
		jkl.setHeaderValue("JKL");
		jkl.setHeaderSpan(3);
		jkl.setModelIndex(3);

		columns.addColumn(abc);
		columns.addColumn(new TableColumn(1));
		columns.addColumn(ghi);
		columns.addColumn(jkl);
		columns.addColumn(new TableColumn(4));
		columns.addColumn(new TableColumn(5));

		JTable table = new JTable(data, columns)
		{
			protected void configureEnclosingScrollPane()
			{
			}
		};

		table.setTableHeader(null);

		TableHeader header = new TableHeader(table);

		header.setForeground(Color.blue);
		header.setFont(header.getFont().deriveFont(18.0f));

		JScrollPane pane = new JScrollPane(table);

		pane.setColumnHeaderView(header);

		JFrame f = new JFrame();

		f.setContentPane(pane);

		f.pack();

		f.setVisible(true);
	}
}


