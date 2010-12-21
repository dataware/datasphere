package datasphere.dataware;

/*
Copyright (C) 2010 J.Goulding, R. Mortier 

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import datasphere.dataware.DSFormatException;

public class DSUpdate {
	
	//-- namespace of dataware source
	private String namespace;
	
	//-- namespace of item, defaulting to "dataware:update"
	private String primaryTag;
	
	//-- namespaces of associated item categories (interfaces)
	private ArrayList< String > tags;
	
	//-- creation time: time that the update was generated by the source
	private Long ctime;

	//-- forwarding time: time that the update was forwarded by the dataware
	private Long ftime;
	
	//-- received time: time that the update was received. 
	private Long rtime;

	//-- the jid of the dataware generating the 
	private String sid;
	
	//-- WGS-84 coordinate attached to the update
	public class Coordinate {
		private double lat;
		private double lon;
		public Coordinate() {};
		public Coordinate( double lat, double lon ) {
			this.lat = lat;
			this.lon = lon;
		}
		public double getLat() { return lat; };
		public double getLon() { return lon; };
	}
	
	private Coordinate loc;
	
	//-- create / read / update / delete
	private String crud;

	//-- a short textual summary of the update
	private String description;

	//-- the total number of items represented by this item type
	private Long total;

	//-- other data associated with the update, specific to its type
	private HashMap< String, String > meta;

	public DSUpdate( String sid, String primaryTag, String crud ) 
	throws DSFormatException {
		setSid( sid );
		setCrud( crud );
		setType( primaryTag );
	}
	
	public DSUpdate( String sid, String primaryTag )
	throws DSFormatException {
		setSid( sid );
		setCrud( "create" );
		setType( primaryTag );
	}

	public DSUpdate( JSONObject json )
	throws IOException, JSONException {
		fromJSON( json );
	}
	
	public String getNamespace() 			{ return this.namespace; }
	public String getPrimaryTag() 			{ return this.primaryTag; }
	public ArrayList< String > getTags() 	{ return this.tags; }
	public Long getCtime() 					{ return this.ctime; }
	public Long getFtime() 					{ return this.ftime; }
	public Long getRtime() 					{ return this.rtime; }
	public Coordinate getLocation() 		{ return this.loc; }
	public String getCrud() 				{ return this.crud; }
	public String getDescription() 			{ return this.description; }
	public String getSid() 					{ return this.sid; }
	public Long getTotal() 					{ return this.total; }
	public HashMap< String, String > getMeta() 	{ return this.meta; }

	public DSUpdate setNamespace( String namespace ) 	 { this.namespace = namespace; return this; }
	public DSUpdate setType( String primaryTag ) 		 { this.primaryTag = primaryTag; return this; }
	public DSUpdate setTags( ArrayList< String > tags )  { this.tags = tags; return this; }
	public DSUpdate setCtime( Long ctime ) 				 { this.ctime = ctime; return this; }
	public DSUpdate setFtime( Long ftime ) 				 { this.ftime = ftime; return this; }
	public DSUpdate setRtime( Long rtime ) 				 { this.rtime = rtime; return this; }
	public DSUpdate setLocation( Coordinate location ) 	 { this.loc = location; return this; }
	public DSUpdate setSid( String sid ) 				 { this.sid = sid; return this; }
	public DSUpdate setDescription( String description ) { this.description = description; return this; }
	public DSUpdate setTotal( long total ) 				 { this.total = total; return this; }
	
	public DSUpdate setLocation( double lat, double lon ) { 
		this.loc = new Coordinate( lat, lon );
		return this; 
	}	

	public String getCtimeAsTime() {
		SimpleDateFormat fmt = new SimpleDateFormat( "h:mma" );
		return fmt.format( new Date( ctime ) ).toLowerCase();
	}
	
	public String getCtimeAsDate() {
		SimpleDateFormat fmt = new SimpleDateFormat( "E dd MMMM" );
		return fmt.format( new Date( ctime ) );
	}
	
	public String getTagsJSON() {
		if ( tags == null ) return null;
		JSONArray j = new JSONArray( tags );
		return j.toString();
	}
	
	public String getMetaJSON() {
		if ( meta == null ) return null;
		JSONObject j = null;
		j = new JSONObject( meta );
		return j.toString();
	}
	
	public String getLocationJSON() {
		if ( loc == null ) return null;
		JSONObject j = null;
		j = new JSONObject( loc );
		return j.toString();
	}
	
	
	public DSUpdate setCrud( String action )
	throws DSFormatException { 
		if ( action.equals( "create" ) || 
			 action.equals( "read" )   || 
			 action.equals( "update" ) || 
			 action.equals( "delete" ) ) {
			this.crud = action; 
			return this;	
		}
		else {
			throw new DSFormatException();
		}
	}

	public DSUpdate addTag( String tag ) {
		
		if ( tags == null ) 
			tags = new ArrayList< String >();
		
		this.tags.add( tag );
		return this;
	}

	public DSUpdate addMetadata( String key, String value ) {
		
		if ( meta == null ) 
			meta = new HashMap< String, String >();
		
		this.meta.put( key, value );
		return this;
	}


	public DSUpdate addMetadata( String key, int intValue) {
		return addMetadata( key, Integer.toString( intValue ) );		
	}
	
	@Override
	public String toString() {
		return toJSON();
	}


	public String toJSON() {
		Gson gson = new GsonBuilder().create();
		String result = gson.toJson( this );
		return result;
	}
		

	public DSUpdate setTags( JSONArray jsonTags ) 
	throws JSONException {
		this.tags = new ArrayList< String >();
		for ( int i = 0; i < jsonTags.length(); i++ ) 
			this.tags.add( jsonTags.getString( i ) );
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public DSUpdate setMeta( JSONObject jsonMeta ) 
	throws JSONException {
		this.meta = new HashMap< String, String >();
		Iterator ji = jsonMeta.keys();
		while ( ji.hasNext() ) {
			String key = ( String ) ji.next();
			String val = jsonMeta.getString( key );
			this.meta.put( key, val );
		}
		return this;
	}
	
	public void fromJSON( JSONObject j ) 
	throws JSONException {
		
		//-- mandatory fields
		this.namespace = j.getString( "namespace" );
		this.primaryTag = j.getString( "primaryTag" );
		this.crud = j.getString( "crud" );
		this.ctime = j.getLong( "ctime" );
		
		//-- expected fields
		if ( j.has( "ftime" ) )
			this.ftime = j.getLong( "ftime" );
		
		//-- fields added after receipt
		if ( j.has( "rtime" ) )
			this.rtime = j.getLong( "rtime" );
		
		if ( j.has( "sid" ) )
			this.sid = j.getString( "sid" );
		
		//-- optional fields
		this.description = j.has( "description" ) ? j.getString( "description" ) : "no description";
		this.total =  j.has( "total" ) ? j.getLong( "total" ) : 0;
		
		if ( j.has( "tags") ) 
			setTags( j.getJSONArray( "tags" ) );
		
		if ( j.has( "meta") ) 
			setMeta( j.getJSONObject( "meta" ) );
		
		if ( j.has( "loc") ) {
			JSONObject jsonLoc = j.getJSONObject( "loc" );
			this.loc = new Coordinate (
				jsonLoc.getLong( "lon" ),
				jsonLoc.getLong( "lat" ) 
			);
		}
	}
		
	
	public String toXML() {
		
		String locString =	( loc == null ) ? "" :
			"<loc>" +
				"<lon>" + loc.getLon() + "</lon>" +
				"<lat>" + loc.getLat() + "</lat>" +
			"</loc>";
		
		String metaXML = "";
		if ( meta != null) {
			metaXML = "<meta>";
			for ( Entry< String, String > e : meta.entrySet() ) { 
				metaXML += 
					"<" + e.getKey() + ">" +
					e.getValue() + 
					"</" + e.getKey() + ">";
			}
			metaXML += "</meta>";
		}		
		
		String tagsXML = "";
		if ( tags != null) 
			for ( String e : tags ) tagsXML += "<tag>" + e + "</tag>";

		
		String s = 
			"<DSUpdate>" + 
				"<namespace>" + namespace + "</namespace>" +
				"<primaryTag>" + primaryTag + "</primaryTag>" +
				"<description>" + description + "</description>" +
				"<crud>" + crud + "</crud>" +
				"<ctime>" + ctime + "</ctime>" +
				"<ftime>" + ftime + "</ftime>" +
				"<rtime>" + rtime + "</rtime>" +
				"<total>" + total + "</total>" +
				locString +
				tagsXML + 
				metaXML +
			"</DSUpdate>";

		return s;
	}


	
}
