package com.healthcare.herplatform.entity;
import java.util.Arrays;

import javax.persistence.*;
//import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "videos")
public class Video {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
  //  @GeneratedValue(generator = "uuid")
  //  @GenericGenerator(name = "uuid", strategy = "uuid2")
	private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "data")
    private byte[] data;

	public Video() {
		super();
	}

	public Video(String name, byte[] data) {
		super();
		this.name = name;
		this.data = data;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Video [id=" + id + ", name=" + name + ", data=" + Arrays.toString(data) + "]";
	}
}







