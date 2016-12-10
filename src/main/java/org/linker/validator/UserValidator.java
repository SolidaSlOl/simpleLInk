/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2016 Mikita Herasiutsin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.linker.validator;

import org.linker.model.domain.User;
import org.linker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * User's validator.
 *
 * @since 1.0
 * @author Mikita Herasiutsin (mikita.herasiutsin@gmail.com)
 * @version $Id$
 */
@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(final Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(final Object o, final Errors errors) {
        User user = (User) o;
        String username = user.getUsername();
        Integer psLength = user.getPassword().length();
        Integer usernameLength = username.length();
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",
            "notEmpty");
        if (usernameLength < 6 || usernameLength > 32) {
            errors.rejectValue("username", "size.userForm.username");
        }
        if (this.userService.findByUsername(username) != null) {
            errors.rejectValue("username", "duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
            "notEmpty");
        if (psLength < 8 || psLength > 32) {
            errors.rejectValue("password", "size.userForm.password");
        }

        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue(
                "passwordConfirm",
                "diff.userForm.passwordConfirm"
            );
        }
    }
}
