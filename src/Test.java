import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kakkun61.wajavassr.FriendHitokoto;
import kakkun61.wajavassr.Wajavassr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;



public class Test
{
	public static void main( String[] args ) throws IOException, ParseException
	{
//		testGetReplies( new Wajavassr( "kakkun61", "YMxgaHA9KclQTtCA" ) );
		testGetFriendTimeline( new Wajavassr( "kakkun61", "YMxgaHA9KclQTtCA" ) );
	}

	private static void testGetReplies( Wajavassr w ) throws IOException, ParseException
	{
		SimpleDateFormat dateForm = new SimpleDateFormat( "yyyy'-'MM'-'dd'('EEE')' HH':'mm':'ss" );
		DateFormatSymbols usSymbols = new DateFormatSymbols( Locale.US );
		DateFormatSymbols nowSymbols = dateForm.getDateFormatSymbols();
		nowSymbols.setShortWeekdays( usSymbols.getShortWeekdays() );
		dateForm.setDateFormatSymbols( nowSymbols );

		JSONArray replies = w.getReplies();
		for( Object r : replies )
		{
			JSONObject reply = (JSONObject)r;
			System.out.println( reply.get( "text" ) );
			System.out.println( "\tby " + ( (JSONObject)reply.get( "user" ) ).get( "screen_name" ) + " (" + reply.get( "user_login_id" ) + ")");
			System.out.println( "\tat " + dateForm.format( new Date( (Long)reply.get( "epoch" )*1000 ) ) );
		}
	}

	private static void testGetFriendTimeline( Wajavassr w ) throws IOException, ParseException
	{
		SimpleDateFormat dateForm = new SimpleDateFormat( "yyyy'-'MM'-'dd'('EEE')' HH':'mm':'ss" );
		DateFormatSymbols usSymbols = new DateFormatSymbols( Locale.US );
		DateFormatSymbols nowSymbols = dateForm.getDateFormatSymbols();
		nowSymbols.setShortWeekdays( usSymbols.getShortWeekdays() );
		dateForm.setDateFormatSymbols( nowSymbols );

		for( FriendHitokoto h : w.getFriendTimelie() )
		{
			System.out.println( h.getText() );
			System.out.println( "\tby " + h.getUserScreenName() + " (" + h.getUserLoginId() + ")");
			System.out.println( "\tat " + dateForm.format( new Date( h.getEpoch()*1000 ) ) );
			System.out.print( "\tfav");
			for( String n : h.getFavorites() )
				System.out.print( " " + n );
			System.out.println();
		}
	}
}
