import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import twitter4j.User;

/**
 * Classe responsavel por gerar as tres listas sumarizadas
 * @author Andr� Oliveira
 *
 */
public class Reports {
	String[] topNParams; /// conjunto de parametros utilizados para listar top 5 usuarios com mais seguidores
	String[] totalTagParams; /// conjunto de parametros utilizados para listar total de #tags
	String[] totalHourParams;/// conjunto de parametros utilizados para listar #tags agrupadas por hora
	
	/**
	 * Construtor recebe os parametros de cada uma das listas que serao geradas
	 * @param topN - parametros da lista top 5 usuarios com mais seguidores
	 * @param topTag - parametros da lista total de #tags
	 * @param topHour - parametros da lista agrupada por hora
	 */
	public Reports(String[] topN, String[] totalTag, String[] totalHour){
		topNParams = topN;
		totalTagParams = totalTag;
		totalHourParams = totalHour;
	}
	
	/**
	 * Dada uma lista de usuarios, ordena e verifica quais sao os que tem mais seguidores
	 * @param users - lista contendo todos os usuarios
	 * @param n - quantidade de usuarios que a lista deve retornar
	 * @return - retorna lista em formato JSON dos N usuarios com mais seguidores
	 */
	public String getTopNUsers(List<User> users, int n)

	{
		// ordena a lista de usuarios de acordo com a quantidade de seguidores
        Collections.sort (users, new Comparator() {
            public int compare(Object o1, Object o2) {
                User u1 = (User) o1;
                User u2 = (User) o2;
                return u1.getFollowersCount() > u2.getFollowersCount() ? -1 : (u1.getFollowersCount() < u2.getFollowersCount() ? +1 : 0);
            }
        });
        
        String topN = "";
        
        // adiciona os N primeiros da lista (com mais seguidores) em uma string em formato JSON
        for(int i = 0; i < n;i++)
        {
        	String[] values = {users.get(i).getScreenName(), ""+ users.get(i).getFollowersCount()};
        	
        	// transforma os parametros/ valores em string (json)
        	topN += Support.getJson(topNParams, values);
        	
        	// adiciona a virgula apos cada um dos resultados, exceto no ultimo, que nao deve ter virgula
        	if(i < n - 1)
        	{
        		topN += ",\n";
        	}
        }
        
        return topN;
	}
	
	/**
	 * Dada uma lista dos resulados das pesquisas das #tags, constr�i uma string
	 * em formato json contendo o total de tweets em portugues para cada #tag
	 * @param searches - lista contendo o resultados das pesquisas de todas as #tags
	 * @return - retorna uma string em formato json contendo 
	 * total de tweets em portugues para cada #tag
	 */
	public String getTotalPT(List<TweetResult> searches)
	{
		String totalPT = "";
		
		for(int i = 0; i < searches.size(); i++)
		{
			// array contendo os valores que serao gerados
			String[] values = {searches.get(i).getHashTag(), "" + searches.get(i).getTotal(), "" + 
							   searches.get(i).getTotalPT()};
			
			// transforma os parametros/valores em string (json)
			totalPT += Support.getJson(totalTagParams, values);
			
			// adiciona a virgula apos cada um dos resultados, exceto no ultimo, que nao deve ter virgula
        	if(i < searches.size() - 1)
        	{
        		totalPT += ",\n";
        	}
		}
		return totalPT;
	}
	
	/**
	 * Dado um array de 24 posi��es (um para cada hora do dia), constroi uma string em 
	 * formato json para apresentar a quantidade de tweets totais por hora do dia
	 * @param arrayHours - array de inteiro de 24 posi��es que contem a quantidade total
	 * de tweets por hora do dia
	 * @return - retorna uma string em formato json contendo 
	 * total de tweets por hora
	 */
	public String getTotalHours(int[] arrayHours)
	{
		String totalHours = "";

		for(int i = 0; i < arrayHours.length; i++)
		{
			// array que contem os valores que serao gerados
			String[] values = {"" + i + "h" ,"" + arrayHours[i]};
			
			// transforma os parametros/valores em string (json)
			totalHours += Support.getJson(totalHourParams, values);
			
			// adiciona a virgula apos cada um dos resultados, exceto no ultimo, que nao deve ter virgula
        	if(i < arrayHours.length - 1)
        	{
        		totalHours += ",\n";
        	}
		}
		return totalHours;
	}
}
