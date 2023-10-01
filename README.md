# Dave's Shader Reference

> No shader is too trivial.

Go to [https://davesmith00000.github.io/shaders/](https://davesmith00000.github.io/shaders/) to run the shaders.

All the shaders are written in [Ultraviolet](https://github.com/PurpleKingdomGames/ultraviolet) and presented running in [Indigo](https://indigoengine.io/).

I'm doing this as a learning exercise and to build up a collection of reference material.

Some of the code will necessarily be based on / adapted from the work of others. I will endevour to attribute the original authors (in the code) wherever possible, please feel free raise an issue on the repo if you feel I haven't done a good enough job in this regard.

## Setting up Mill using Millw

[Millw](https://github.com/lefou/millw) is already in the repo, and should 'just work' as a drop in replacement for the normal Mill executable, but just in case, you can install it like this:

```sh
curl -L https://raw.githubusercontent.com/lefou/millw/0.4.7/millw > mill && chmod +x mill
```

## Running the shaders locally

The shaders are arranged in a tree based on their folders, with the 'shaders' folder ommited. So to run the campfire shader, you would look at the directory structure, and do:

```sh
./mill shaders.demos.campfire.runGame
./mill 'shaders.noise.white-noise.runGame'
```

Note the single quotes that allow you to run the hyphenated module in zsh.

If you'd like to compile and test everything, you can do the following:

```sh
./mill shaders.__.compile
./mill shaders.__.test
```

## Regenerating the website

This command re-generates the website into the docs folder. It will take quite a while...

```sh
./mill shaders.genSite
```

If you then `cd docs/`, you can run the following to run the site locally.


```sh
npm install http-server
npx http-server -c-1 
```

## Further reading

Nothing I've done here or anywhere else with regard to shaders is original.

Here are a few links to shader related goodness, by people much cleverer than myself.

- [https://thebookofshaders.com/](https://thebookofshaders.com/)
- [https://iquilezles.org/articles/](https://iquilezles.org/articles/)
- [https://github.com/ashima/webgl-noise](https://github.com/ashima/webgl-noise)
- [https://www.shadertoy.com/](https://www.shadertoy.com/)

