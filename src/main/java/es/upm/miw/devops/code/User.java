package es.upm.miw.devops.code;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "family_name")
    private String familyName;
    private String email;
    private String identity;
    private String address;
    private String city;
    private String province;
    @Column(name = "postal_code")
    private String postalCode;
    private Boolean active;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public User() {
        // empty for JPA
    }

    public User(String id, String firstName, String familyName, String email, String identity, String address,
                String city, String province, String postalCode, Boolean active) {
        this(id, firstName, familyName, email, identity, address, city, province, postalCode, active, null);
    }

    public User(String id, String firstName, String familyName, String email, String identity, String address,
                String city, String province, String postalCode, Boolean active, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.familyName = familyName;
        this.email = email;
        this.identity = identity;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.active = active;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isBillable() {
        return hasContent(this.firstName) && hasContent(this.familyName) && hasContent(this.email)
                && hasContent(this.identity) && hasContent(this.address) && hasContent(this.city)
                && hasContent(this.province) && hasContent(this.postalCode);
    }

    private static boolean hasContent(String value) {
        return value != null && !value.isBlank();
    }
}