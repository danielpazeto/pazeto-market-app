package com.pazeto.ceasapazeto.vo;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

public class Client extends BaseDB implements Serializable {
	public static final String TABLE_NAME = "Cliente";
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String LASTNAME = "lastname";
	public static final String TELEPHONE = "telephone";
	public static final String PHONE1 = "cellphone1";
	public static final String PHONE2 = "cellphone2";
	public static final String HANGAR = "hangar";
	public static final String POSITION = "position";
	public static final String CITY = "city";
	public static final String ADDRESS = "address";
	public static final String CPF_CNPJ = "cpf_cnpj";
	public static final String CREATED_BY = "created_by";
	public static final String OBSERVACAO = "observacao";
	public static final String _UPDATED = "_updated";
	public static final String _DELETED = "_deleted";

	// @formatter:off
	public static final String CREATE_TABLE_CLIENT = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME
			+ "( "
			+ ID + " INTEGER PRIMARY KEY,"
			+ NAME + " text not null ,"
			+ LASTNAME + " text ,"
			+ TELEPHONE	+ " integer ,"
			+ PHONE1 + " integer ,"
			+ PHONE2 + " integer ,"
			+ HANGAR + " text ,"
			+ POSITION + " text,"
			+ CITY + " text,"
			+ ADDRESS + " text,"
			+ CPF_CNPJ + " integer,"
			+ OBSERVACAO + " text,"
			+ CREATED_BY + " integer,"
			+ _UPDATED + " integer,"
			+ _DELETED + "BOOLEAN )";
	// @formatter:on
	public static final String DROP_TABLE_CLIENT = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	long id;
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
	String cpf_cnpj;
	String created_by;
	String observacao;
	// String _update;
	String _deleted;

	public Client(Cursor c) {
		this.setId(c.getLong(c.getColumnIndex(ID)));
		this.setName(c.getString(c.getColumnIndex(NAME)));
		this.setLastname(c.getString(c.getColumnIndex(LASTNAME)));
		// TODO Setar todas as variáveis
	}

	public Client() {
		// Default constructor
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getCpf_cnpj() {
		return cpf_cnpj;
	}

	public void setCpf_cnpj(String cpf_cnpj) {
		this.cpf_cnpj = cpf_cnpj;
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

	@Override
	public ContentValues getAsContentValue() {
		ContentValues values = new ContentValues();
		values.put(Client.NAME, this.getName());
		values.put(Client.LASTNAME, this.getLastname());
		values.put(Client.OBSERVACAO, this.getDescription());
		values.put(Client.TELEPHONE, this.getTelephone());
		values.put(Client.PHONE1, this.getCellPhone1());
		values.put(Client.PHONE2, this.getCellPhone2());
		values.put(Client.HANGAR, this.getHangar());
		values.put(Client.POSITION, this.getPosition());
		values.put(Client.CITY, this.getCity());
		values.put(Client.ADDRESS, this.getAddress());
		values.put(Client.CPF_CNPJ, this.getCpf_cnpj());
		values.put(Client.CREATED_BY, this.getCreated_by());
		return values;
	}

}
