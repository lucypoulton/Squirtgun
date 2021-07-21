/*
 * Copyright © 2021 Lucy Poulton
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")

    testImplementation("net.kyori:adventure-api:4.8.1")
    testImplementation("net.kyori:adventure-text-serializer-gson:4.8.1")
    testImplementation("net.kyori:adventure-text-serializer-legacy:4.8.1")
    testImplementation("com.google.guava:guava:21.0")

    api("net.kyori:adventure-api:4.8.1")
    api("net.kyori:adventure-text-serializer-legacy:4.8.1")
    compileOnlyApi("com.google.guava:guava:21.0") {
        because("It's the version Minecraft is bundled with")
    }
    compileOnlyApi("com.google.code.gson:gson:2.8.0") {
        because("It's the version Minecraft is bundled with")
    }
    compileOnlyApi("org.jetbrains:annotations:21.0.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
