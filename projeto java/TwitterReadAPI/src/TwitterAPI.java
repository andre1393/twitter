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
	Twitter twitterSearch; // objeto responsavel pela busca pelas ultimas #tags
	TwitterStream twitterStream; // objeto responsavel pela busca por stream
	
	/**
	 * Construtor recebe os valores de autentica��o e os parametros a serem buscados
	 * @param consumerKey - chave de autentica��o
	 * @param consumerSecret - chave de autentica��o
	 * @param accessToken - chave de autentica��o
	 * @param accessTokenSecret - chave de autentica��o
	 * @param params_
	 */
	public TwitterAPI(String consumerKey, String consumerSecret, 
					  String accessToken, String accessTokenSecret)
	{
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
	public TweetResult getLastTweet(String key, String[] params, twitterUser users, int[] totalHours)
	{	
		String searchResult = ""; /// resultado da busca pela #tag
		int totalPT = 0; /// total de tweets em portugues, por #tag
		int total = 0; /// total de tweets por #tag
		
        try 
        {
        	Query querySearch = new Query(key);
        	querySearch.count(100); // quantidade maxima de resultados da busca
        	QueryResult resultSearch = twitterSearch.search(querySearch); // resultado da busca
        	List<Status> tweets = resultSearch.getTweets(); // lista com todos os tweets encontrados
        	
        	total = tweets.size();
        	
        	// monta uma string em formato json
        	for (Status tweetSearch : tweets) {
        		//adiciona o usuario � lista de usuarios, para mais tarde gerar a lista de usuarios com mais seguidores
        		users.addUser(tweetSearch.getUser());
        		
        		// monta o resultado atual em formato json
        		String[] values = {key, tweetSearch.getUser().getScreenName(), "" + tweetSearch.getCreatedAt().getHours()};
        		searchResult += Support.getJson(params, values) + ",\n";
        		
        		// incrementa o total de tweets em portugues para a #tag pesquisada
        		if(tweetSearch.getLang().equals("pt")) totalPT++;
        		
        		// incrementa a quantidade de tweets por hora
        		try{
        			int hour = tweetSearch.getCreatedAt().getHours();
        			totalHours[hour]++; 
        		}catch(Exception err){}
        	}
        	
        	// retira a ultima virgula do json, para nao dar erro
        	searchResult = searchResult.substring(0, searchResult.length() - 2); 
        }catch (TwitterException err) {
            err.printStackTrace();
        }
        
        return new TweetResult(key, searchResult, total, totalPT, totalHours);
	}
	
	/**
	 * inicia a aquisi��o dos tweets via streaming de acordo com as #tags passadas
	 * @param keyWords - lista de #tags a serem monitoradas 
	 */
	public void startStreaming(String[] keyWords, String[] params, String fullFileName)
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
        		
        		TwitterFile.writeFile(fullFileName, searchResult, true, true);
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
