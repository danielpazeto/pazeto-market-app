package com.pazeto.comercio.vo;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Comparator;

public class Client implements Serializable {

    @Exclude
    String id;
    String name;
    String lastname;
    String description;
    String telephone;
    String phone1;
    String phone2;
    String hangar;
    String position;
    String city;
    String address;
    String cpf;
    String cnpj;
    String created_by;
    String observacao;
    String _deleted;

    public Client() {
        // Default constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCellPhone1() {
        return phone1;
    }

    public void setCellPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getCellPhone2() {
        return phone2;
    }

    public void setCellPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String tel) {
        this.telephone = tel;
    }

    public String getHangar() {
        return hangar;
    }

    public void setHangar(String hangar) {
        this.hangar = hangar;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String get_deleted() {
        return _deleted;
    }

    public void set_deleted(String _deleted) {
        this._deleted = _deleted;
    }

    public String getFullname() {
        return getName() + " " + getLastname();
    }

    public static class ClientNameComparator implements Comparator<Client>  {

        public int compare(Client client1, Client client2) {
            String clientName1 = client1.getName().toUpperCase();
            String clientName2 = client2.getName().toUpperCase();

            return clientName1.compareTo(clientName2);
        }
    };
}
