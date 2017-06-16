import java.util.ArrayList;
import java.util.List;

import twitter4j.User;

/**
 * Classe representa a lista de usuarios
 * @author Andr� Oliveira
 *
 */
public class twitterUser {
	private List<User> listUsers;/// lista de todos os usuarios encontrados ao buscar pelas #tags
	
	/**
	 * construtor padr�o
	 */
	public twitterUser(){
		listUsers = new ArrayList<User>();
	}
	
	/**
	 * Adiciona um usu�rio na lista de usu�rios, mas verifica antes
	 * se ele ainda nao foi incluido, para evitar duplicidade
	 * @param user - usuario a ser incluido
	 */
	public void addUser(User user)
	{
		boolean exists = false;
		
		// varre a lista em busca do usuario
		for(int i = 0; i < listUsers.size() && !exists; i++)
		{
			if(listUsers.get(i).getScreenName().equals(user.getScreenName())) exists = true;
		}
		
		// se o usuario nao foi encontrado na lista, entao adiciona
		if(!exists) listUsers.add(user);
	}
	
	/**
	 * todos os usuarios listados
	 * @return - retorna a lista de usuarios
	 */
	public List<User> getUsers()
	{
		return listUsers;
	}
	
}
