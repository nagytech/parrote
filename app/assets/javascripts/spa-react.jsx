// Get the mount point for rendering all bonmots
var mountNode = document.getElementById('bonmots')

// New BonMot Item React Class
var BonMotItem = React.createClass({
    // Render implementation
    // - Renders a bonmot item from the owner iterateable collection
    render: function() {
        return (

            <div className="panel panel-default bonmot">
                <div className="panel-body">
                    <span className="words">{this.props.item.text}</span>
                </div>
                <div className="panel-footer">
                    <div className="small">
                        <span className="pull-right"><em>{moment(this.props.item.createdOn).format('ddd MMM DD HH:mm:ss')}</em></span>
                        <span className="pull-left"><a href={'/user/' + this.props.item.username}>@{this.props.item.username}</a></span>
                        <div className="clearfix"></div>
                    </div>
                </div>
            </div>

        );
    }
});

var BonMotList = React.createClass({
    // Get the initial state
    getInitialState: function() {
      // Empty array of items
      return { items: [] };
    },
    // Implemented functionality after the component mounts
    componentDidMount: function() {
        // Call external plugin with injected rjs object
        $.fn.bonmotReact(this);
    },
    // Render implementation
    // - Renders a list of bonmot items from the items prop of the state
    render: function() {
        return (
            <div className="bonmot-list">
                {
                    this.state.items.map(function(item) {
                        return <BonMotItem item={item} />
                    })
                }
            </div>
        );
    }
});

// Initialize the render of a bonmotlist at the render mount node
React.render(<BonMotList />, mountNode);