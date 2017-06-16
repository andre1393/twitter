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
	private static String[] params = {"hashTag", "userName", "time"}; /// parametros que ser�o salvos
	private static String fullFileName = "allHashTags.json"; /// arquivo onde serao gravadas todas as #tags

	private static String[] topNparams = {"userName", "followers"}; /// parametros da lista top N usuarios
	private static String[] topTagsParams = {"hashTag", "total", "totalPT"}; /// parametros da lista total de #tags
	private static String[] topHourParams = {"hour", "total"};/// parametros da lista total #tags por hora
	
	private static String topUsersFile = "topUsers.json";///nome do arquivo que armazena os usuarios com mais seguidores
	private static String totalPTFile = "totalPT.json";/// nome do arquivo que armazena a quantidade de tweets em portugues
	private static String totalHoursFile = "totalHours.json";/// nome do arquivo que armazena a quantidade de tweets por hora
	/**
	 * m�todo main do programa
	 * @param args - parametros de linha de comando
	 */
	public static void main(String args[])
	{
		// deleta os arquivos json para evitar que eles sejam sobreescritos
		String[] files = {topUsersFile, totalPTFile, totalHoursFile};
		TwitterFile.deleteFiles(files);
		
		keyWords = getHashTagList(args);/// lista de #tags
		twitterUser users = new twitterUser(); /// lista de usuarios criadores das #tags buscadas
		int[] arrayHours = new int[24];/// array que contem o total de tweets por hora do dia (uma posi��o para cada hora do dia)
		
		String[] tokens = {"mh9Xu2es833iwrPe2zKSclA0t", 
				           "XQMxg6osgdAQWtlPU1ipsi0FBPl7YLHl9PtJOpnyJG2YWwA3ps",
				           "873392543953715202-Q3rhLtTWfkTZQgCmYATPPqJOwvafvrS",
				           "nH6yxkAuT9puFEzikWs6GOCUxxuEbFQwjQHz3qbihQ28B"}; // chaves de seguran�a
		
		// objeto que realiza a busca de #tags no twitter
		TwitterAPI twitterAPI = new TwitterAPI(tokens[0], tokens[1], tokens[2], tokens[3]);
		
		// objeto que contem o resultado das buscas de cada #tag
		List<TweetResult> allResult = new ArrayList<TweetResult>();
		
		// realiza uma busca para cada uma das #tags indicadas
		for(int i = 0; i < keyWords.length; i++)
		{
			// lista de tweets para a #tag buscada
			allResult.add(twitterAPI.getLastTweet(keyWords[i], params, users, arrayHours)); 
			String tweets = allResult.get(allResult.size() - 1).getSearch();
			
			// o trecho a seguir cria um arquivo contendo o resultado de cada busca, e um que contem todos juntos
			// como esses arquivos nao sao necessarios, esse trecho esta comentado
			
			/*
			// escreve os tweets dessa #tag em um arquivo json
			TwitterFile.writeFile(keyWords[i].replace('#', '_') + ".json", tweets, true, true);
			
			boolean endFile;
			if(i == (keyWords.length - 1))endFile = true;
			else endFile = false;
			
			// escreve todos tweets em um unico arquivo
			TwitterFile.writeFile(fullFileName, tweets, true, endFile);
			*/
		}
		
		Reports r = new Reports(topNparams, topTagsParams, topHourParams);/// objeto que gera as listas desejadas
		
		// lista dos top 5 usuarios com mais seguidores e salva em um arquivo json
		String top5 = r.getTopNUsers(users.getUsers(), 5);
		TwitterFile.writeFile(topUsersFile, top5, true, true);
		
		// lista o total de tweets em portugues por #tag e salva em um arquivo json
		String totalPT = r.getTotalPT(allResult);
		TwitterFile.writeFile(totalPTFile, totalPT, true, true);
		
		// lista o total de tweets por hora do dia e salva em um arquivo json
		String totalHours = r.getTotalHours(arrayHours);
		TwitterFile.writeFile(totalHoursFile, totalHours, true, true);
		
		
		System.out.println("fim do programa.");
		//twitterAPI.startStreaming(keyWords, fullFileName); 
		
		System.exit(1);
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
