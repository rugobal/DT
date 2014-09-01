package com.rugobal.dt.client.css;

import com.google.gwt.user.cellview.client.CellTable;

public interface MyCellTableResources extends CellTable.Resources {
	
	// All css in https://code.google.com/p/google-web-toolkit/source/browse/trunk/user/src/com/google/gwt/user/cellview/client/CellTable.css?r=9513
	
    @Override
    @Source({CellTable.Style.DEFAULT_CSS, "myCellTableResources.css" }) 
    CellTable.Style cellTableStyle(); 
}
