package com.tech.arinzedroid.starchoiceadmin.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.tech.arinzedroid.starchoiceadmin.model.AdminModel;
import com.tech.arinzedroid.starchoiceadmin.model.AgentsModel;
import com.tech.arinzedroid.starchoiceadmin.model.ClientsModel;
import com.tech.arinzedroid.starchoiceadmin.model.CompletedProducts;
import com.tech.arinzedroid.starchoiceadmin.model.ProductsModel;
import com.tech.arinzedroid.starchoiceadmin.model.TransactionsModel;
import com.tech.arinzedroid.starchoiceadmin.model.UserProductsModel;
import com.tech.arinzedroid.starchoiceadmin.repo.AppRepo;

import java.util.List;

public class AppViewModel extends ViewModel {

    private AppRepo appRepo;
    private LiveData<List<UserProductsModel>> userProductsList;
    private LiveData<List<AgentsModel>> agentsLiveData;
    private LiveData<List<ProductsModel>> productsModelLiveData;
    private LiveData<List<TransactionsModel>> transactionLiveData;
    private LiveData<List<TransactionsModel>> agentsTransactionLiveData;
    private LiveData<List<ClientsModel>> allClientModelLiveData;
    private LiveData<List<ClientsModel>> agentsClientsLiveData;
    private LiveData<List<CompletedProducts>> completedProductsLiveDataList;
    private MutableLiveData<String> query = new MutableLiveData<>();


    public  AppViewModel(){
       appRepo = new AppRepo();
    }

    public LiveData<AdminModel> loginAdmin(String username, String password){
        return appRepo.loginAdmin(username,password);
    }

    public LiveData<List<AgentsModel>> getAllAgents(boolean refresh){
        if(refresh){
            agentsLiveData = appRepo.getAllAgents();
        }else{
            if(agentsLiveData == null){
                agentsLiveData = appRepo.getAllAgents();
            }
        }
        return agentsLiveData;
    }

    public LiveData<List<ProductsModel>> getAllProducts(boolean refresh){
        if(!refresh){
            if(productsModelLiveData == null){
                productsModelLiveData = appRepo.getAllProducts();
            }
        }else{
            productsModelLiveData = appRepo.getAllProducts();
        }
        return productsModelLiveData;
    }

    public LiveData<Boolean> addAgents(AgentsModel agentsModel){
        return appRepo.addAgent(agentsModel);
    }

    public LiveData<Boolean> addProducts(ProductsModel productsModel){
        return appRepo.addProducts(productsModel);
    }

    public LiveData<List<TransactionsModel>> getAllTransactions(boolean refresh){
        if(!refresh){
            if(transactionLiveData == null){
                transactionLiveData = appRepo.getAllTransactions();
            }
        }else{
            transactionLiveData = appRepo.getAllTransactions();
        }
        return transactionLiveData;
    }

    public LiveData<List<TransactionsModel>> getAgentTransactions(String agentId, boolean refresh){
        if(!refresh){
            if(agentsTransactionLiveData == null){
                agentsTransactionLiveData = appRepo.getAgentTransactions(agentId);
            }
        }else{
            agentsTransactionLiveData = appRepo.getAgentTransactions(agentId);
        }
        return agentsTransactionLiveData;
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

    public LiveData<List<ClientsModel>> getAllClients(boolean refresh){
        if(!refresh){
            if(allClientModelLiveData == null){
                allClientModelLiveData = appRepo.getAllClients();
            }
        }else{
            allClientModelLiveData = appRepo.getAllClients();
        }
        return allClientModelLiveData;
    }

    public LiveData<List<ClientsModel>> getAgentsClients(String agentId,boolean refresh){
        if(!refresh){
            if(agentsClientsLiveData == null){
                agentsClientsLiveData = appRepo.getAgentClients(agentId);
            }
        }else{
            agentsClientsLiveData = appRepo.getAgentClients(agentId);
        }
        return agentsClientsLiveData;
    }

    public LiveData<Boolean> deleteClients(ClientsModel clientsModel){
        return appRepo.deleteClient(clientsModel);
    }

    public LiveData<Boolean> deleteUserProduct(UserProductsModel userProductsModel){
        return appRepo.deleteUserProduct(userProductsModel);
    }

    public LiveData<List<CompletedProducts>> getCompletedProducts(boolean refresh){
        if(!refresh){
            if(completedProductsLiveDataList == null){
                completedProductsLiveDataList = appRepo.getCompletedProducts();
            }
        }else{
            completedProductsLiveDataList = appRepo.getCompletedProducts();
        }
        return completedProductsLiveDataList;
   }

    public LiveData<List<UserProductsModel>> getUserProducts(String userId, boolean refresh){
        if(!refresh){
            if(userProductsList == null){
                userProductsList = appRepo.getUserProducts(userId);
            }
        }else{
            userProductsList = appRepo.getUserProducts(userId);
        }
        return userProductsList;
   }

    public LiveData<Boolean> addUserProducts(List<UserProductsModel> userProductsModels){
        return appRepo.addUserProducts(userProductsModels);
   }

    public LiveData<List<TransactionsModel>> getUserTransactionByProduct(String productId,String userId, boolean refresh){
        if(!refresh){
            if(transactionLiveData == null){
                transactionLiveData = appRepo.getUserTransactionsByProduct(productId,userId);
            }
        }else{
            transactionLiveData = appRepo.getUserTransactionsByProduct(productId,userId);
        }
        return transactionLiveData;
   }

    public LiveData<Boolean> updateTransactionItem(TransactionsModel transactionsModel,
                                                   UserProductsModel userProductsModel){
        return appRepo.updateTransactionItem(transactionsModel,userProductsModel);
    }

    public LiveData<Boolean> deleteTransactionItem(TransactionsModel transactionsModel){
        return appRepo.deleteTransactionItem(transactionsModel);
    }

    public LiveData<String> getQuery() {
        return query;
    }

    public void setQuery(String query){
        this.query.setValue(query);
    }

    public LiveData<Boolean> updateClient(ClientsModel clientsModel){
        return appRepo.updateClient(clientsModel);
    }
}
