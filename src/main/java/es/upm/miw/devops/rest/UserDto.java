package es.upm.miw.devops.rest;

import es.upm.miw.devops.code.User;

public record UserDto(
        String id,
        String firstName,
        String familyName,
        String email,
        String identity,
        String address,
        String city,
        String province,
        String postalCode,
        Boolean active) {

    public static UserDto of(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getFamilyName(), user.getEmail(),
                user.getIdentity(), user.getAddress(), user.getCity(), user.getProvince(), user.getPostalCode(),
                user.getActive());
    }

    public User toUser() {
        return new User(this.id, this.firstName, this.familyName, this.email, this.identity, this.address,
                this.city, this.province, this.postalCode, this.active);
    }
}