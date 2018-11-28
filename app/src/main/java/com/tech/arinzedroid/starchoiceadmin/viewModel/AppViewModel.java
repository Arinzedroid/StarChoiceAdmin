package com.tech.arinzedroid.starchoiceadmin.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.tech.arinzedroid.starchoiceadmin.model.AdminModel;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.model.CompletedProducts;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.model.TransactionsModel;
import com.tech.arinzedroid.starchoiceadmin.repo.AppRepo;

import java.util.List;

import static com.tech.arinzedroid.starchoiceadmin.model.CompletedProducts.*;

public class AppViewModel extends ViewModel {

    private AppRepo appRepo;

    public  AppViewModel(){
       appRepo = new AppRepo();
    }

    public LiveData<AdminModel> loginAdmin(String username, String password){
        return appRepo.loginAdmin(username,password);
    }

    public LiveData<List<AgentsModel>> getAllAgents(){
        return appRepo.getAllAgents();
    }

    public LiveData<List<ProductsModel>> getAllProducts(){
        return appRepo.getAllProducts();
    }

    public LiveData<Boolean> addAgents(AgentsModel agentsModel){
        return appRepo.addAgent(agentsModel);
    }
    public LiveData<Boolean> addProducts(ProductsModel productsModel){
        return appRepo.addProducts(productsModel);
    }

    public LiveData<List<TransactionsModel>> getAllTransactions(){
        return appRepo.getAllTransactions();
    }

    public LiveData<List<TransactionsModel>> getAgentTransactions(String agentId){
        return appRepo.getAgentTransactions(agentId);
    }

    public LiveData<Boolean> deleteAgent(AgentsModel agentsModel){
        return appRepo.deleteAgent(agentsModel);
    }

    public LiveData<Boolean> updateAgent(AgentsModel agentsModel){
        return appRepo.updateAgent(agentsModel);
    }

    public LiveData<Boolean> deleteProduct(ProductsModel productsModel){
        return appRepo.deleteProduct(productsModel);
    }

    public LiveData<Boolean> updateProduct(ProductsModel productsModel){
        return appRepo.updateProduct(productsModel);
    }

    public LiveData<List<ClientsModel>> getAllClients(){
        return appRepo.getAllClients();
    }

    public LiveData<List<ClientsModel>> getAgentsClients(String agentId){
        return appRepo.getAgentClients(agentId);
    }

    public LiveData<Boolean> deleteClients(ClientsModel clientsModel){
        return appRepo.deleteClient(clientsModel);
    }

   public LiveData<List<CompletedProducts>> getCompletedProducts(){
        return appRepo.getCompletedProducts();
   }
}
