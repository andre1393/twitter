import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;

/**
 * classe principal do projeto
 * @author Andr� Oliveira da Silva
 */
public class Main {

	private static String[] keyWords;/// cont�m as #tags que ser�o pesquisadas
	private static String[] params = {"-user: ", "-text: ", "-id: ", "-time: "}; /// paramtros que ser�o salvos
	private static String fullFileName = "allHashTags.txt";
	/**
	 * m�todo main do programa
	 * @param args - parametros de linha de comando
	 */
	public static void main(String args[])
	{
		keyWords = getHashTagList(args);
		
		String[] tokens = {"mh9Xu2es833iwrPe2zKSclA0t", 
				           "XQMxg6osgdAQWtlPU1ipsi0FBPl7YLHl9PtJOpnyJG2YWwA3ps",
				           "873392543953715202-Q3rhLtTWfkTZQgCmYATPPqJOwvafvrS",
				           "nH6yxkAuT9puFEzikWs6GOCUxxuEbFQwjQHz3qbihQ28B"}; // chaves de seguran�a
		
		TwitterAPI twitterAPI = new TwitterAPI(tokens[0], tokens[1], tokens[2], tokens[3], params);
		
		for(int i = 0; i < keyWords.length; i++)
		{
			String hashTags = twitterAPI.getLastTweet(keyWords[i]);
			TwitterFile.writeFile(keyWords[i].replace('#', '_') + ".txt", hashTags);
			TwitterFile.writeFile(fullFileName, hashTags);
		}
		
		twitterAPI.startStreaming(keyWords, fullFileName);        
	}
	
	/**
	 * retorna as #tags que serao buscadas
	 * @param args - argumentos passados na inicializa��o do prgrama
	 * @return - lista string com as #tags
	 */
	public static String[] getHashTagList(String[] args)
	{
		List<String> list = new ArrayList<String>();/// lista que contem as keyWords
		String[] keys;
		// os parametros passados para atribuir aos keyWords
		if(args.length > 0)
		{
			for(int i = 0; i < args.length; i++)
			{
				list.add(args[i]);
			}
			keys = new String[list.size()];
			keys = list.toArray(keys);			
		}else{// se nao tiver nenhum parametro, mantem as chaves padr�o.
			list.add("#brasil");
			list.add("#brazil");
			list.add("#brasil2017");
			list.add("#brazil2017");
			list.add("#carnaval");
			list.add("#tourism");
			list.add("#bahia");
			list.add("#riodejaneiro");
			list.add("#saopaulo");
			
			keys = new String[list.size()];
			keys = list.toArray(keys);
		}
		
		return keys;
	}
}
