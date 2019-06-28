package repository;



import entity.Authority;

public interface AuthorityRepository {

	Authority findByName(String name);
	
}

