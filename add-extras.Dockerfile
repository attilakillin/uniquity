# Add extra layers to the built backend application, adding extra functionality.

# Argument which image to copy systemd from
ARG systemd_image

# Argument, which image to extend.
ARG image

# 1: Install systemd files so that the backend can access them.
FROM $systemd_image AS systemd-files


# 2: Create a custom user with a specified name, and copy systemd files.

FROM $image
# Argument, what should the new user's name be.
ARG username=default
# Add group and user.
RUN addgroup -S $username && adduser -S $username -G $username
# Set user as active.
USER $username

# Argument, which systemd folder do we copy
ARG systemd_path
# Copy systemd files
COPY --from=systemd-files $systemd_path /systemd
