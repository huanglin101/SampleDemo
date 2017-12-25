package com.demo.oauth2.service;

import java.util.List;

import com.demo.oauth2.entity.Client;


public interface ClientService {
	   public Client createClient(Client client);
	    public Client updateClient(Client client);
	    public void deleteClient(Long clientId);

	    Client findOne(Long clientId);

	    List<Client> findAll();

	    Client findByClientId(String clientId);
	    Client findByClientSecret(String clientSecret);

}
