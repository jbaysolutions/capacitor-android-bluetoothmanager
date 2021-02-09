const description = 'Capacitor plugin to allow access to the underlying Bluetooth Manager.'
const title = 'Capacitor Android BluetoothManager'

module.exports = {
	base: "/capacitor-android-bluetoothmanager/",
	locales: {
		'/': {
		  lang: 'en-US',
		  title: title,
		  description: description
		},
	  },
	head: [
		['link', { rel: 'icon', href: `/favicon.ico` }],
		['link', { rel: "apple-touch-icon", sizes: "180x180", href: "https://jbaysolutions.github.io/capacitor-android-bluetoothmanager/assets/favicon/apple-touch-icon.png"}],
	],
	port: 8081,
	theme: '@vuepress/vue',
	themeConfig: {
		smoothScroll: true,
		logo: '/assets/img/logo.png',
		repo: 'jbaysolutions/capacitor-android-bluetoothmanager',
		docsDir: 'website/docs',
		editLinks: true,
		algolia: {
		  apiKey: '2f143d1edd24605564065dd02bf0a22b',
		  indexName: 'vue_grid_layout'
		},
		locales: {
			'/': {
				selectText: 'Languages',
				label: 'English',
				ariaLabel: 'Select language',
				sidebar: {
					'/guide/': [
						{
							title: "Guide",
							collapsable: false,
							children: [
								'',
								'usage',
								'methods',
								'events',
								'support',
							]
						},
						/*{
							title: "Examples",
							collapsable: false,
							children: []
						}*/
					]
				},
				nav: [
					{text: 'Home', link: '/'},
					{text: 'Guide', link: '/guide/'},
					{text: 'Changelog', link: '/changelog/'}
				],
				searchPlaceholder: 'Search...',
				editLinkText: 'Help improve this page!',
				lastUpdated: 'Last Updated'
			}
		}
	},
	plugins: [
		'@vuepress/back-to-top',
		['@vuepress/google-analytics', {
			ga: 'UA-37288388-25' // UA-00000000-0
		}],
		['seo', {
			title: $page => `${$page.title} — Capacitor Android BluetoothManager`,
			siteTitle: (_, $site) => $site.title,
			description: $page => $page.frontmatter.description || description,
			author: (_, $site) => $site.themeConfig.author,
			tags: $page => $page.frontmatter.tags,
			twitterCard: _ => 'summary_large_image',
			type: () => 'article',
			url: (_, $site, path) => ($site.themeConfig.domain || '') + path,
			publishedAt: $page => $page.frontmatter.date && new Date($page.frontmatter.date),
			modifiedAt: $page => $page.lastUpdated && new Date($page.lastUpdated),
		}],
		['vuepress-plugin-serve', {
			port: 8080,
			staticOptions: {
				dotfiles: 'allow',
			},
			/*beforeServer(app, server) {
				app.get('/path/to/my/custom', function(req, res) {
					res.json({ custom: 'response' })
				})
			},*/
		}],
	],
	dest: 'public',
}
