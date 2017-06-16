/**
 * Classe de suporte, que contem m�todos de apoio � aplica��o
 * @author Andr� Oliveira
 *
 */
public class Support {
	
	/**
	 * construtor padr�o
	 */
	public Support(){}
	
	/**
	 * Dados uma lista de parametros/valores constroe uma string em formato json
	 * @param params - parametros do json
	 * @param values - valores do json
	 * @return - String em formato json com os valores passados no m�todo
	 */
	public static String getJson(String[] params, String[] values){
		String json = "{\n\t";
		
		for(int i = 0; i < params.length && i < values.length; i++)
		{
			json += "\"" + params[i] + "\": \"" + values[i];
			if(i < params.length - 1 && i < values.length - 1)
			{
				json += "\",\n\t";
			}else{
				json += "\"\n}";
			}
		}
		return json;
	}
}
