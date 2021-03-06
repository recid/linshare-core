/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 * 
 * Copyright (C) 2014 LINAGORA
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version, provided you comply with the Additional Terms applicable for
 * LinShare software by Linagora pursuant to Section 7 of the GNU Affero General
 * Public License, subsections (b), (c), and (e), pursuant to which you must
 * notably (i) retain the display of the “LinShare™” trademark/logo at the top
 * of the interface window, the display of the “You are using the Open Source
 * and free version of LinShare™, powered by Linagora © 2009–2014. Contribute to
 * Linshare R&D by subscribing to an Enterprise offer!” infobox and in the
 * e-mails sent with the Program, (ii) retain all hypertext links between
 * LinShare and linshare.org, between linagora.com and Linagora, and (iii)
 * refrain from infringing Linagora intellectual property rights over its
 * trademarks and commercial brands. Other Additional Terms apply, see
 * <http://www.linagora.com/licenses/> for more details.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License and
 * its applicable Additional Terms for LinShare along with this program. If not,
 * see <http://www.gnu.org/licenses/> for the GNU Affero General Public License
 * version 3 and <http://www.linagora.com/licenses/> for the Additional Terms
 * applicable to LinShare software.
 */
package org.linagora.linshare.view.tapestry.components;


import java.util.UUID;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.json.JSONObject;
import org.linagora.linshare.view.tapestry.objects.JSONRaw;

/**
 * Extends the Window to allow for easy effects on the object
 * The main feature is to be able to set the javascript id
 * @author ncharles
 *
 */
public class WindowWithEffects extends Window {
	

	/* ***********************************************************
     *                         Parameters
     ************************************************************ */
    @Parameter(value="true", defaultPrefix=BindingConstants.PROP)
    private boolean closable;

    /* ***********************************************************
     *                      Injected services
     ************************************************************ */

	/* ***********************************************************
     *                Properties & injected symbol, ASO, etc
     ************************************************************ */

	// the javascript id of the component
	@Retain
	private String JSONId; 
	
	
    /* ***********************************************************
     *                       Phase processing
     ************************************************************ */
     
    /* ***********************************************************
     *                   Event handlers&processing
     ************************************************************ */

	
	
	
	    
	public WindowWithEffects() {
		super();
		// create a unique javascript id

		this.JSONId="window_" + UUID.randomUUID().toString();
	}
	
	
	protected void configure(JSONObject options) {
		options.put("id", getJSONId());
		options.put("draggable", true);
		options.put("minimizable", false);
		options.put("maximizable", false);
		options.put("showEffect", new JSONRaw("Element.show"));
		options.put("hideEffect", new JSONRaw("Element.hide"));
		options.put("destroyOnClose", false);
        options.put("closable", closable);
	}


	
    /* ***********************************************************
     *                      Getters & Setters
     ************************************************************ */ 
	
	public String getJSONId() {
		return JSONId;
	}


	
}
