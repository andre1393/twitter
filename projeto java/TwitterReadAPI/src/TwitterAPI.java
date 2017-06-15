import java.util.List;

import twitter4j.FilterQuery;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Implenta��o dos m�todos para leitura dos dados do twitter
 * @author Andr� Oliveira
 */
public class TwitterAPI {
	
	ConfigurationBuilder cb; // configuration builder para a busca por ultimas #tags
	ConfigurationBuilder cb_stream; // configuration builder para a busca por stream
	String params[]; // parametros que ser�o salvos
	Twitter twitterSearch; // objeto responsavel pela busca pelas ultimas #tags
	TwitterStream twitterStream; // objeto responsavel pela busca por stream
	
	public TwitterAPI(String consumerKey, String consumerSecret, 
					  String accessToken, String accessTokenSecret, 
					  String[] params_)
	{
		params = params_;
		cb = new ConfigurationBuilder();
		cb_stream = new ConfigurationBuilder();
		
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(consumerKey)
		.setOAuthConsumerSecret(consumerSecret)
		.setOAuthAccessToken(accessToken)
		.setOAuthAccessTokenSecret(accessTokenSecret);
		
		cb_stream.setDebugEnabled(true)
		.setOAuthConsumerKey(consumerKey)
		.setOAuthConsumerSecret(consumerSecret)
		.setOAuthAccessToken(accessToken)
		.setOAuthAccessTokenSecret(accessTokenSecret);
		
        twitterSearch = new TwitterFactory(cb.build()).getInstance();
        twitterStream = new TwitterStreamFactory(cb_stream.build()).getInstance();
		
	}
	 
	/**
	 * busca pelos ultimos 100 tweets dada uma #tag
	 * @param key - #tag a ser buscada
	 * @return uma string contendo todos os resultados da busca, separados por parametros
	 */
	public String getLastTweet(String key)
	{	
		String searchResult = ""; // resultado da busca
		
        try 
        {
        	Query querySearch = new Query(key);
        	querySearch.count(100); // quantidade maxima de resultados da busca
        	QueryResult resultSearch = twitterSearch.search(querySearch); // resultado da busca
        	List<Status> tweets = resultSearch.getTweets(); // lista com todos os tweets encontrados
        	for (Status tweetSearch : tweets) {
        		searchResult += (params[0] + "@" + tweetSearch.getUser().getScreenName() + 
        						 params[1] + tweetSearch.getText().replaceAll("\\n", "") + 
        						 params[2] + tweetSearch.getId() +
        						 params[3] + tweetSearch.getCreatedAt().getHours() + "\n");
        	}        
        }catch (TwitterException err) {
            err.printStackTrace();
            System.out.println("Falha ao pesquisar pela #tag: " + err.getMessage());
        }
        
        return searchResult;
	}
	
	/**
	 * inicia a aquisi��o dos tweets via streaming de acordo com as #tags passadas
	 * @param keyWords - lista de #tags a serem monitoradas 
	 */
	public void startStreaming(String[] keyWords, String fullFileName)
	{
		// listener para a mudan�a de status
		StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStatus(Status status) {
            	User user = status.getUser();
                
                // gets Username
                String username = status.getUser().getScreenName();
                String searchResult = "";
        		searchResult += (params[0] + "@" + status.getUser().getScreenName() + 
						 params[1] + status.getText().replaceAll("\\n", "") + 
						 params[2] + status.getId() +
						 params[3] + status.getCreatedAt().getHours() + "\n");
        		
        		TwitterFile.writeFile(fullFileName, searchResult);
            }

            @Override
            public void onTrackLimitationNotice(int arg0) {
                // TODO Auto-generated method stub

            }

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}

        };
        
        
        FilterQuery fq = new FilterQuery();

        fq.track(keyWords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq); 
	}
}
