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

/**
 * Generic Datasphere exception class, indicating that an error has
 * been thrown somewhere within the framework. 
 * 
 * @author James Goulding
 * @version 2010-03-11
 */
public class DSException 
extends Exception
{
	private static final long serialVersionUID = 1014530909804487992L;

	public DSException( String s ) {
		super( s );
	}
	
	public DSException( Exception e ) {
		super( e );
	}

	public DSException() {
		super();
	}
}

// End ///////////////////////////////////////////////////////////////